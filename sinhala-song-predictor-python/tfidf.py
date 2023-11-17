import joblib

import preprocess as pre


def execute_tfidf_svm(text):
    corpus = preprocess_text(text)
    print(corpus)
    loaded_tfidf_vectorizer = joblib.load('resources/tfidf_vectorizer.pkl')
    loaded_model = joblib.load('models/svm_model.pkl')

    new_data_tfidf = loaded_tfidf_vectorizer.transform([corpus])
    predictions = loaded_model.predict(new_data_tfidf)
    print("Y pred:", predictions[0])
    return {
        "prediction": check_label(predictions[0])
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
    # text_tokens = pre.tokenize_text(text)


def check_label(label):
    if label == 0:
        return "Calm"
    elif label == 1:
        return "Happy"
    else:
        return "Sad"
