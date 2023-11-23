import preprocess as pre
import requests

API_URL = "https://api-inference.huggingface.co/models/malinda135/xlm-roberta-base-sinhala-song-cls"
headers = {"Authorization": "Bearer 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", "Content-Type": "application/json"}

def execute_bert(text):
    print("Started execute_bert")
    text = preprocess_text(text)
    print(text)
    output = query({
        "text": "විදී පිය සෙනෙහසට කව් ගී ලියැවුනා මදී සිරුරේ"
    })
    print(output)
    return output


def query(payload):
    response = requests.post(API_URL, headers=headers, json=payload)
    return response.json()
def preprocess_text(text):
    text = pre.line_break_replace(text)
    text = pre.remove_escape_sequences(text)
    text = pre.remove_digits(text)
    text = pre.remove_html_tags(text)
    text = pre.remove_special_characters(text)
    text = pre.replace_full_stops(text)
    text = pre.remove_english_letters(text)
    return text
