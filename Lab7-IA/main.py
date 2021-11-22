import numpy as np


def sigmoid(x):
    # sigmoid activation function
    return 1 / (1 + np.exp(-x))


input_data = []
input_label = []


def parse_input(file):
    # function in order to read the input file
    file0 = open(file, 'r')
    lines = file0.readlines()
    for line in lines:
        input_data.append(list(map(int, line.split(" "))))
    for i in range(len(input_data)):
        input_label.append(input_data[i][-1])
        del input_data[i][-1]


number_of_epochs = 11000
input_layer_size = 2
hidden_layer_size = 2
output_layer_size = 2
learning_rate = 0.1


def softmax(x):
    # function in order to compute the weighted probabilities from the output layer
    e_x = np.exp(x - np.max(x))
    return e_x / e_x.sum(axis=0)


# We split the input data into a list of values that represent the training data (input_data) and a list of values
# that represents the outcome (input_label)
parse_input("and.txt")
input_data = np.asarray(input_data)
input_data = input_data.reshape((len(input_data), input_layer_size, 1))
input_label = np.asarray(input_label)

print("Input training data:\n", input_data)
print("Labels for input training data:", input_label)

first_layer = np.zeros((input_layer_size, 1))
hidden_layer = np.zeros((hidden_layer_size, 1))
last_layer = np.zeros((output_layer_size, 1))

# In order for the sigmoid function to activate the neurons properly, we must constraint the weights - randomly - in
# order for the slope of the sigmoid function to be in the effective range (-4,4), and also reformatting it to be -
# in this case - 2x2
first_layer_weights = np.random.normal(0, 1 / np.sqrt(input_layer_size), (hidden_layer_size, input_layer_size))
hidden_layer_weights = np.random.normal(0, 1 / np.sqrt(hidden_layer_size), (output_layer_size, hidden_layer_size))

# Same for the biases, but this time, the distribution is 2x1
first_layer_biases = np.random.normal(0, 1 / np.sqrt(input_layer_size), (hidden_layer_size, 1))
hidden_layer_biases = np.random.normal(0, 1 / np.sqrt(hidden_layer_size), (output_layer_size, 1))


def front_propagation():
    # Front propagation function - we go through each layer of our network, computing the first layer weighted sum,
    # the hidden layer weighted sum and, finally, the weighted probability distribution for the last layer
    global hidden_layer, last_layer
    first_layer_weighted_sum = np.dot(first_layer_weights, first_layer) + first_layer_biases
    hidden_layer = sigmoid(first_layer_weighted_sum)
    hidden_layer_weighted_sum = np.dot(hidden_layer_weights, hidden_layer) + hidden_layer_biases
    last_layer = softmax(hidden_layer_weighted_sum)


def back_propagation():
    # Back propagation function - for the first two layers, we compute the corrections in order for our network to
    # learn and adapt its parameters to fit the binary function demands (to adjust the geometric line coordinates)
    global hidden_layer_weights_corrections, hidden_layer_biases_corrections, first_layer_weights_corrections, \
        first_layer_biases_corrections
    hidden_layer_weights_corrections += np.dot(last_layer_error, hidden_layer.T)
    hidden_layer_biases_corrections += last_layer_error
    hidden_layer_error = (hidden_layer * (1 - hidden_layer)) * np.dot(hidden_layer_weights.T, last_layer_error)
    first_layer_weights_corrections += np.dot(hidden_layer_error, first_layer.T)
    first_layer_biases_corrections += hidden_layer_error


def update_weights():
    # Based on the corrections we computed in the back propagation phase, we update the weights of the delimiter line
    # in order to get closer to 100% accuracy and learn the binary function. Based on the chosen learning rate which,
    # here, has to be small in order to gradually get closer to the spot we want our weights and biases to be,
    # we compute and update accordingly
    global hidden_layer_weights, hidden_layer_biases, first_layer_weights, first_layer_biases
    hidden_layer_weights -= hidden_layer_weights_corrections * learning_rate / len(input_data)
    hidden_layer_biases -= hidden_layer_biases_corrections * learning_rate / len(input_data)
    first_layer_weights -= first_layer_weights_corrections * learning_rate / len(input_data)
    first_layer_biases -= first_layer_biases_corrections * learning_rate / len(input_data)


def calculate_accuracy():
    # A function in order to compute the accuracy of our network. It is computed by propagating forward through our
    # layers and comparing our predictions to the actual labels of the input
    global first_layer
    count = 0
    for each in range(len(input_data)):
        first_layer = input_data[each]
        front_propagation()
        result = np.argmax(last_layer)
        if result == input_label[each]:
            count += 1
    percent = count * 100 / len(input_data)
    return percent


for epoch in range(number_of_epochs):
    # We must choose a high number of epochs, since the learning rate is so small and our dataset is small,
    # we must make lots of small updates to the biases/weights until we reach the proper values
    print("Epoch", epoch + 1)

    first_layer_weights_corrections = np.zeros((hidden_layer_size, input_layer_size))
    hidden_layer_weights_corrections = np.zeros((output_layer_size, hidden_layer_size))
    first_layer_biases_corrections = np.zeros((hidden_layer_size, 1))
    hidden_layer_biases_corrections = np.zeros((output_layer_size, 1))

    for index in range(len(input_data)):
        # We propagate forward and backward each input entry from our input file, in order to extract the last
        # layer's error and update the weights and biases
        first_layer = input_data[index]
        front_propagation()

        last_layer_error = last_layer - input_label[index]
        back_propagation()

    update_weights()

    accuracy = calculate_accuracy()
    print("Accuracy:", accuracy)
    if accuracy == 100:
        break
