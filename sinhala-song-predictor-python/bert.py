from transformers import pipeline

import preprocess as pre


def execute_bert(text):
    print("Started execute_bert")
    text = preprocess_text(text)
    pipe = init_bert()
    result = pipe(text)

    predicted_label = result[0]
    print("From Bert:" + predicted_label)
    return predicted_label


def init_bert():
    return pipeline("text-classification", model="malinda135/xlm-roberta-base-sinhala-song-cls", return_all_scores=True)


def preprocess_text(text):
    text = pre.line_break_replace(text)
    text = pre.remove_escape_sequences(text)
    text = pre.remove_digits(text)
    text = pre.remove_html_tags(text)
    text = pre.remove_special_characters(text)
    text = pre.replace_full_stops(text)
    text = pre.remove_english_letters(text)
    return text
