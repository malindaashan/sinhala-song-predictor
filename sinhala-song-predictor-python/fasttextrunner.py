import preprocess as pre
#import fasttext
def execute_fasttext(text):
    print("started executing fasttext")
    text = preprocess_text(text)
    model_path = '/opt/research/fast-text.bin'
    model = ""
    #model = fasttext.load_model(model_path)
    prediction = model.predict(text)
    print(prediction[0][0])
    result=prediction[0][0]
    finalresult=result.split("__")[2]
    print("Result Fasttext:"+result.split("__")[2])
    return {
        "prediction": finalresult
   }


def preprocess_text(text):
    text = pre.line_break_replace(text)
    text = pre.remove_escape_sequences(text)
    text = pre.remove_digits(text)
    text = pre.remove_html_tags(text)
    text = pre.remove_special_characters(text)
    text = pre.replace_full_stops(text)
    text = pre.remove_english_letters(text)
    return text
