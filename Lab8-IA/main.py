import lightrdf

word = input("Please enter a word: ")


def print_everything():
    for trip in parser.parse("C:\\Users\\ROG\\Downloads\\CSO.3.3.owl", base_iri=None):
        if prepare_data(trip)[1] == "superTopicOf":
            print(prepare_data(trip))


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
# print_everything()
for triple in parser.parse("C:\\Users\\ROG\\Downloads\\CSO.3.3.owl", base_iri=None):
    copy_triple = prepare_data(triple)
    if copy_triple[1] == "superTopicOf" and copy_triple[2] == word:
        print(triple)
        # continue
