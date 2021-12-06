import random

import gym
import numpy as np

# We import the environment from Open AI gym
env = gym.make("FrozenLake-v1")
env.render()

# We have 4 different actions with 16 different spaces, so we will have a matrix with 16 columns and 4 rows
state_space_size = env.observation_space.n
action_space_size = env.action_space.n

# Creating a Q-Table and initialising all values as 0
q_table = np.zeros((state_space_size, action_space_size))
print(q_table)

# Number of episodes
num_episodes = 10000
# Max number of steps per episode
max_steps_per_episode = 100

# α value
# Just like in supervised learning, alpha is the extent to which the Q-values are being updated in each iteration
learning_rate = 0.1
# γ value
# Determines how much importance we want to give to future rewards. A high value means long-term effective award, while a low value renders the agent greedy
discount_rate = 0.99

# Greedy strategy
# ε value
exploration_rate = 1
# Lower and higher bounds
max_exploration_rate = 1
min_exploration_rate = 0.01
exploration_decay_rate = 0.01

# List to contain all the rewards of all the episodes given to the agent
rewards_all_episodes = []

for episode in range(num_episodes):  # Contains that happens in an episode
    # Initialize the new episode parameters
    state = env.reset()

    done = False  # Tells us whether episode is finished
    rewards_current_episode = 0
    for step in range(max_steps_per_episode):  # Contains that happens in a time step

        # Exploration-exploitation trade off
        # To combat overfitting, instead of just selecting the best learned Q-value action, we will sometimes favor exploring the action space further (lower eps means episodes with more penalties)
        # To decide whether to pick a random action or exploit an already computed action, we compare epsilon to a randomly generated value between 0 and 1
        exploration_rate_threshold = random.uniform(0, 1)

        if exploration_rate_threshold > exploration_rate:
            # Exploit
            action = np.argmax(q_table[state, :])
        else:
            # Explore
            action = env.action_space.sample()

        # Take the chosen action by calling our env object and pass the action to it
        # The returned touple is formed by the new state, its reward, whether the action ended our episode and diagnostic info helpful in debugging
        new_state, reward, done, info = env.step(action)

        # Update Q-table for Q(state,action)
        # Basically, a weighted sum between the sum of the old value and the "learned" value
        q_table[state, action] = q_table[state, action] * (1 - learning_rate) + learning_rate * (reward + discount_rate * np.max(q_table[new_state, :]))

        # Update the current state and update the rewards
        state = new_state
        rewards_current_episode += reward
        if done:  # Checking if episode is over
            break
    # Exploration rate decay
    # Once an episode is finished, we need to update the exploration rate using exponential decay, so the exploration rate decreases at a rate proportional to its current value
    exploration_rate = min_exploration_rate + (max_exploration_rate - min_exploration_rate) * np.exp(
        -exploration_decay_rate * episode)
    rewards_all_episodes.append(rewards_current_episode)

# Calculate and print the average reward per thousand episodes
# Progress can be seen, as the average reward per 1000 episodes increases from 0.16 to 0.7
rewards_per_thousand_episodes = np.split(np.array(rewards_all_episodes), num_episodes / 1000)
count = 1000

print("******** Average reward per thousand episodes ********\n")
for r in rewards_per_thousand_episodes:
    print(count, ": ", str(sum(r / 1000)))
    count += 1000
# Print the updates Q-Table
print("\n\n******* Q-Table *******\n")
print(q_table)
