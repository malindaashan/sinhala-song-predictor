import unicodedata
import re
import nltk


def line_break_replace(text):
    return text.replace("\r\n", " ")


def remove_escape_sequences(text):
    return "".join(ch for ch in text if unicodedata.category(ch)[0] != "C")


def remove_digits(text):
    return ''.join([i for i in text if not i.isdigit()])


def remove_html_tags(text):
    p = re.compile(r'<.*?>')
    return p.sub('', text)


def remove_special_characters(sentence, keep_apostrophes=True):
    sentence = sentence.strip()
    if keep_apostrophes:
        PATTERN = r'[?|$|&|*|%|@|(|)|~]'
        filtered_sentence = re.sub(PATTERN, r'', sentence)
        return filtered_sentence
    return sentence


def replace_full_stops(sentence):
    return sentence.replace(".", " ")


def remove_english_letters(sentence):
    non_english_sentence = re.sub(r'[a-zA-Z]', '', sentence)
    return non_english_sentence


def tokenize_text(text):
    tokens = nltk.word_tokenize(text)
    tokens = [token.strip() for token in tokens]
    return tokens
