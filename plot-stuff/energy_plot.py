import matplotlib.pyplot as plt
import math
import csv

x = []
y_no_obstacle = []
y_obstacle_center_multiplier_2 = []
y_obstacle_center_multiplier_3 = []
y_obstacle_center_multiplier_4 = []
y_obstacle_center_multiplier_5 = []

y_obstacle_left_multiplier_2 = []
y_obstacle_left_multiplier_3 = []

y_obstacle_right_multiplier_2 = []
y_obstacle_right_multiplier_3 = []

y_obstacle_right_right_multiplier_2 = []
y_obstacle_right_right_multiplier_3 = []

with open('fall-reentrega/proms/prom; No obstacle; energy;.csv', 'r') as csvfile:
    plots = csv.reader(csvfile, delimiter=',')
    for row in plots:
        x.append(float(row[0]))
        y_no_obstacle.append(math.log10(float(row[1])))


with open('fall-reentrega/proms/prom; Obstacle center; energy; multiplier = 2.csv', 'r') as csvfile:
    plots = csv.reader(csvfile, delimiter=',')
    for row in plots:
        y_obstacle_center_multiplier_2.append(math.log10(float(row[1])))

with open('fall-reentrega/proms/prom; Obstacle center; energy; multiplier = 3.csv', 'r') as csvfile:
    plots = csv.reader(csvfile, delimiter=',')
    for row in plots:
        y_obstacle_center_multiplier_3.append(math.log10(float(row[1])))

with open('fall-reentrega/proms/prom; Obstacle center; energy; multiplier = 4.csv', 'r') as csvfile:
    plots = csv.reader(csvfile, delimiter=',')
    for row in plots:
        y_obstacle_center_multiplier_4.append(math.log10(float(row[1])))

with open('fall-reentrega/proms/prom; Obstacle center; energy; multiplier = 5.csv', 'r') as csvfile:
    plots = csv.reader(csvfile, delimiter=',')
    for row in plots:
        y_obstacle_center_multiplier_5.append(math.log10(float(row[1])))



with open('fall-reentrega/proms/prom; Obstacle left; energy; multiplier = 2.csv', 'r') as csvfile:
    plots = csv.reader(csvfile, delimiter=',')
    for row in plots:
        y_obstacle_left_multiplier_2.append(math.log10(float(row[1])))

with open('fall-reentrega/proms/prom; Obstacle left; energy; multiplier = 3.csv', 'r') as csvfile:
    plots = csv.reader(csvfile, delimiter=',')
    for row in plots:
        y_obstacle_left_multiplier_3.append(math.log10(float(row[1])))



with open('fall-reentrega/proms/prom; Obstacle right; energy; multiplier = 2.csv', 'r') as csvfile:
    plots = csv.reader(csvfile, delimiter=',')
    for row in plots:
        y_obstacle_right_multiplier_2.append(math.log10(float(row[1])))

with open('fall-reentrega/proms/prom; Obstacle right; energy; multiplier = 3.csv', 'r') as csvfile:
    plots = csv.reader(csvfile, delimiter=',')
    for row in plots:
        y_obstacle_right_multiplier_3.append(math.log10(float(row[1])))



with open('fall-reentrega/proms/prom; Obstacle right right; energy; multiplier = 2.csv', 'r') as csvfile:
    plots = csv.reader(csvfile, delimiter=',')
    for row in plots:
        y_obstacle_right_right_multiplier_2.append(math.log10(float(row[1])))

with open('fall-reentrega/proms/prom; Obstacle right right; energy; multiplier = 3.csv', 'r') as csvfile:
    plots = csv.reader(csvfile, delimiter=',')
    for row in plots:
        y_obstacle_right_right_multiplier_3.append(math.log10(float(row[1])))                                                


## GROUP
plt.scatter(x, y_no_obstacle, label="No obstacle", marker="o", color="green")

# plt.scatter(x, y_obstacle_center_multiplier_2, label="C = (W/2, 2*R)", marker="x", color="red")
# plt.scatter(x, y_obstacle_center_multiplier_3, label="C = (W/2, 3*R)", marker="x", color="yellow")
# plt.scatter(x, y_obstacle_center_multiplier_4, label="C = (W/2, 4*R)", marker="x", color="blue")
# plt.scatter(x, y_obstacle_center_multiplier_5, label="C = (W/2, 5*R)", marker="x", color="black")

# plt.scatter(x, y_obstacle_left_multiplier_2, label="C = (W/2 - R, 2*R)", marker="x", color="red")
# plt.scatter(x, y_obstacle_left_multiplier_3, label="C = (W/2 - R, 3*R)", marker="x", color="yellow")

# plt.scatter(x, y_obstacle_right_multiplier_2, label="C = (W/2 + R, 2*R)", marker="v", color="blue")
# plt.scatter(x, y_obstacle_right_multiplier_3, label="C = (W/2 + R, 3*R)", marker="v", color="black")

# plt.scatter(x, y_obstacle_right_right_multiplier_2, label="C = (W/2 + 2*R, 2*R)", marker="x", color="red")
# plt.scatter(x, y_obstacle_right_right_multiplier_3, label="C = (W/2 + 2*R, 3*R)", marker="x", color="yellow")
## GROUP


FONTSIZE = 20
plt.legend(loc='lower right', fontsize=FONTSIZE)
plt.ylabel('Energy [J]', fontsize=FONTSIZE)
plt.xlabel('Time [s]', fontsize=FONTSIZE)
plt.tick_params(axis='both', which='major', labelsize=FONTSIZE)

plt.show()