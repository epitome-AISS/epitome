import logging
import io
import json
import argparse
import re
import string
from paddlenlp import Taskflow
from langdetect import detect
from wordcloud import WordCloud
from minio import Minio

for handler in logging.root.handlers[:]:
    logging.root.removeHandler(handler)
logging.basicConfig(filename='word_cloud.log',
                    format = '%(asctime)s - %(levelname)s - %(name)s - %(message)s',
                    datefmt = '%m/%d/%Y %H:%M:%S',
                    level = logging.INFO)
logger = logging.getLogger(__name__)

def SaveImage(image, experiment_id, group_id, user_id, element_id):
    minio_client = Minio('127.0.0.1:9000', access_key="ailab_experiment", secret_key="aiforia123", secure=False)
    bucket_name = f"experiment{experiment_id}"
    if minio_client.bucket_exists(bucket_name) == False:
        try:
            minio_client.make_bucket(bucket_name)
            policy = {
                "Version": "2012-10-17",
                "Statement": [{
                "Effect": "Allow",
                "Principal": {"AWS": ["*"]},
                "Action": ["s3:GetBucketLocation", "s3:ListBucket", "s3:ListBucketMultipartUploads"],
                "Resource": [f"arn:aws:s3:::{bucket_name}"]
                }, {
                "Effect": "Allow",
                "Principal": {"AWS": ["*"]},
                "Action": ["s3:DeleteObject", "s3:GetObject", "s3:ListMultipartUploadParts", "s3:PutObject", "s3:AbortMultipartUpload"],
                "Resource": [f"arn:aws:s3:::{bucket_name}/*"]
                }]
            }
            minio_client.set_bucket_policy(bucket_name, json.dumps(policy))
            logger.info(f'create minio bucket {bucket_name}, policy {policy}')
        except Exception as e:
            logger.error(f'Fail to create bucket {bucket_name} due to {e}')
            return None

    image_byte_array = io.BytesIO()
    image.save(image_byte_array, format='JPEG')
    image_byte_array.seek(0)

    obj_name = f'{group_id}/{element_id}/{user_id}.jpg'
    result = minio_client.put_object(bucket_name, obj_name, image_byte_array, length=-1, part_size=5*1024*1024)
    url = f'http://61.169.23.150:9000/{bucket_name}/{obj_name}'
    return url

def GenCloudImage(words):
    wordcloud = WordCloud(font_path='msyh.ttc', background_color='white').generate(words)
    return wordcloud.to_image()

def replace_punctuation(text):
    # 定义中文标点符号的正则表达式
    chinese_punctuation = '[\u3000\u3001\u3002\u3003\uFF01\uFF0C\u300B\uFF0E\u300D\u300F\u3011\uFF08\uFF09\u3014\u3015\u3010\u3016\u3017\u3008\u3009\u301A\u301B\u3018\u3019\u2018\u2019\u201C\u201D\u2013\u2014\u2026\u2018\u2019\u201C\u201D\uFF07\uFF1F\uFF02\uFF01\u300A\u300B\u300C\u300D\u300E\u300F\u2018\u2019\u201C\u201D\u2014\u2026\u2013\uFF0C\uFF0E\u3000]'

    # 使用正则表达式匹配所有英文和中文标点符号，并替换为空字符串
    return re.sub(f'[{re.escape(string.punctuation)}{chinese_punctuation}]', ' ', text)

def CutText(text):
    #seg = pkuseg.pkuseg(model_name='medicine', postag=True)
    #seg = pkuseg.pkuseg(postag=True)
    seg = Taskflow("pos_tagging")
    #cleaned_text = replace_punctuation(text)
    seg_list = seg(text)
    logger.info(f'cut: {seg_list}')
    ret = []
    skip_tag = set(['q','r', 'c', 'u', 'xc', 'd', 'p', 'w', 'f'])
    for w, t in seg_list:
        if t not in skip_tag:
            ret.append(w)
    return " ".join(ret)

if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument(
        "-t",
        "--text",
        help="input text",
        dest="text",
        type=str,
    )
    parser.add_argument(
        "-e",
        "--experiment",
        help="experiment id",
        dest="experiment_id",
        type=str,
    )
    parser.add_argument(
        "-g",
        "--group",
        help="group id",
        dest="group_id",
        type=str,
    )
    parser.add_argument(
        "-l",
        "--element",
        help="element id",
        dest="element_id",
        type=str,
    )
    parser.add_argument(
        "-u",
        "--user",
        help="user id",
        dest="user_id",
        type=str,
    )
    #img = GenCloudImage('西瓜 苹果 西瓜 西瓜 西红柿 橙子 橙子 西瓜 香蕉 葡萄 葡萄 西瓜 火龙果 火龙果 橙子 西瓜 橙子 橙子 柚子')
    args = parser.parse_args()
    #print(detect(args.text))
    if detect(args.text) == 'en':
        cut_text = args.text
        #print('-----English')
    else:
        cut_text = CutText(args.text)
    #print(f'----{cut_text}')
    img = GenCloudImage(cut_text)
    #url = SaveImage(img, 1, 10, 'EP14-23001', '232323')
    url = SaveImage(img, args.experiment_id, args.group_id, args.user_id, args.element_id)
    ret = {'code':0, 'msg':'success','data':{}}
    if url is None:
        ret = {'code':-1, 'msg':'upload to minio exception','data':{}}
    else:
        ret['data']['url'] = url
    print(json.dumps(ret))

