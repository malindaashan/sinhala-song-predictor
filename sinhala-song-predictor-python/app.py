from flask import Flask, request
from fasttext import execute_fasttext
from tfidf import execute_tfidf_svm
app = Flask(__name__)


@app.route('/')
def hello_world():  # put application's code here
    return 'This is sinhala song predictor backend!!'


@app.route('/nlp/predict/fasttext', methods=['POST'])
def one_hot():
    text = request.json['text']
    execute_fasttext(text)
    return "OK"

@app.route('/nlp/predict/tfidf/svm', methods=['POST'])
def tfidf_svm():
    text = request.json['text']
    return execute_tfidf_svm(text)


if __name__ == '__main__':
    app.run()
