import nltk
from nltk.stem import WordNetLemmatizer
import lightrdf

f = open("D:\\PycharmProjects\\Lab8-IA\\computer-science.txt", "r", encoding="utf-8")
# print(f.read())
tokens = nltk.word_tokenize(f.read())
tagged = nltk.pos_tag(tokens)
file = open("fisier.txt", "w", encoding="utf-8")
file.close()
print(tagged)
good = 0
sentence = ""
file = open("fisier.txt", "w", encoding="utf-8")
for tag in tagged:
    if tag[0] == ".":
        if good == 3:
            file.write(sentence + ".\n")

        good = 0
        sentence = ""

    else:
        sentence = sentence + " " + tag[0]
        if tag[1][0] == "N":
            if good == 0:
                good = good + 1
            if good == 2:
                good = good + 1
        if tag[1] == "VB" or tag[1] == "VBD" or tag[1] == "VBZ":
            if good == 1:
                good = good + 1

wordnet_lemmatizer = WordNetLemmatizer()
wordnet_lemmatizer.lemmatize('subtantivul meu')


def prepare_data(trip):
    subject, relation, obj = trip
    subject = subject.split("/")
    subject = subject[-1]
    relation = relation.split("/")
    relation = relation[-1]
    relation = relation.split("#")
    relation = relation[-1]
    obj = obj.split("/")
    obj = obj[-1]
    obj = obj.split("#")
    obj = obj[-1]
    obj = obj.split('"')
    obj = obj[0]
    trip = (subject, relation, obj)
    return trip


parser = lightrdf.Parser()  # or lightrdf.xml.Parser() for xml
file = open("fisier.txt", "r", encoding="utf-8")
output_file = open("output.txt", "w", encoding="utf-8")
output_file.close()
output_file = open("output.txt", "w", encoding="utf-8")
for line in file:
    copy_line = line
    line = line.split(" ")
    line.pop(0)
    lexical = 0
    for word in line:
        for triple in parser.parse("C:\\Users\\ROG\\Downloads\\CSO.3.3.owl", base_iri=None):
            copy_triple = prepare_data(triple)
            if copy_triple[2] == wordnet_lemmatizer.lemmatize(word):
                print("am gasit")
                output_file.write(copy_line)
                lexical = 1
                break
        if lexical == 1:
            break
