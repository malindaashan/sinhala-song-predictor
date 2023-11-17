from flask import Flask, request
from onehot import execute_onehot
app = Flask(__name__)


@app.route('/')
def hello_world():  # put application's code here
    return 'This is sinhala song predictor backend!!'


@app.route('/nlp/predict/one-hot', methods=['POST'])
def one_hot():
    text = request.json['text']
    execute_onehot(text)
    return "OK"


if __name__ == '__main__':
    app.run()
