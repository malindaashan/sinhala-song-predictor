from flask import Flask, request
from fasttextrunner import execute_fasttext
from tfidf import execute_tfidf_svm
from bert import execute_bert

app = Flask(__name__)


@app.route('/')
def hello_world():  # put application's code here
    return 'This is sinhala song predictor python backend!!'

@app.route('/nlp/predict/tfidf/svm', methods=['POST'])
def tfidf_svm():
    text = request.json['text']
    return execute_tfidf_svm(text)

@app.route('/nlp/predict/bert', methods=['POST'])
def bert():
    text = request.json['text']
    return execute_bert(text)

@app.route('/nlp/predict/fasttext', methods=['POST'])
def one_hot():
    text = request.json['text']
    execute_fasttext(text)
    return  execute_fasttext(text)

if __name__ == '__main__':
    app.run()
