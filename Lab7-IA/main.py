import numpy as np


def sigmoid(x):
    return 1 / (1 + np.exp(-x))


def parse_input(path):
    formatted_input = []
    with open(path) as f:
        lines = f.readlines()
        for entry in lines:
            temp_list = [int(x) for x in entry.replace('\n', '').split()]
            formatted_input.append(temp_list)
        print(formatted_input)


parse_input('venv/input.txt')
