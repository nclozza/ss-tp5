import matplotlib.pyplot as plt
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

with open('fall-reentrega/proms/prom; No obstacle.csv', 'r') as csvfile:
    plots = csv.reader(csvfile, delimiter=',')
    for row in plots:
        y_no_obstacle.append(float(row[1]))


with open('fall-reentrega/proms/prom; D = 0.15; multiplier = 2.csv', 'r') as csvfile:
    plots = csv.reader(csvfile, delimiter=',')
    for row in plots:
        x.append(float(row[0]))
        y_obstacle_center_multiplier_2.append(float(row[1]))

with open('fall-reentrega/proms/prom; D = 0.15; multiplier = 3.csv', 'r') as csvfile:
    plots = csv.reader(csvfile, delimiter=',')
    for row in plots:
        y_obstacle_center_multiplier_3.append(float(row[1]))

with open('fall-reentrega/proms/prom; D = 0.15; multiplier = 4.csv', 'r') as csvfile:
    plots = csv.reader(csvfile, delimiter=',')
    for row in plots:
        y_obstacle_center_multiplier_4.append(float(row[1]))

with open('fall-reentrega/proms/prom; D = 0.15; multiplier = 5.csv', 'r') as csvfile:
    plots = csv.reader(csvfile, delimiter=',')
    for row in plots:
        y_obstacle_center_multiplier_5.append(float(row[1]))



with open('fall-reentrega/proms/prom; Obstacle Left; multiplier = 2.csv', 'r') as csvfile:
    plots = csv.reader(csvfile, delimiter=',')
    for row in plots:
        y_obstacle_left_multiplier_2.append(float(row[1]))

with open('fall-reentrega/proms/prom; Obstacle Left; multiplier = 3.csv', 'r') as csvfile:
    plots = csv.reader(csvfile, delimiter=',')
    for row in plots:
        y_obstacle_left_multiplier_3.append(float(row[1]))



with open('fall-reentrega/proms/prom; Obstacle Right; multiplier = 2.csv', 'r') as csvfile:
    plots = csv.reader(csvfile, delimiter=',')
    for row in plots:
        y_obstacle_right_multiplier_2.append(float(row[1]))

with open('fall-reentrega/proms/prom; Obstacle Right; multiplier = 3.csv', 'r') as csvfile:
    plots = csv.reader(csvfile, delimiter=',')
    for row in plots:
        y_obstacle_right_multiplier_3.append(float(row[1]))



with open('fall-reentrega/proms/prom; Obstacle Right right; multiplier = 2.csv', 'r') as csvfile:
    plots = csv.reader(csvfile, delimiter=',')
    for row in plots:
        y_obstacle_right_right_multiplier_2.append(float(row[1]))

with open('fall-reentrega/proms/prom; Obstacle Right right; multiplier = 3.csv', 'r') as csvfile:
    plots = csv.reader(csvfile, delimiter=',')
    for row in plots:
        y_obstacle_right_right_multiplier_3.append(float(row[1]))                                                



# ERRORS
errors = []
with open('fall-reentrega/errors/errors; Obstacle Right right; multiplier = 3.csv', 'r') as csvfile:
    plots = csv.reader(csvfile, delimiter=',')
    for row in plots:
        errors.append(float(row[1]))


## INDIVIDUAL WITH ERRORS
# plt.plot(x, y_no_obstacle, label="No obstacle")

# plt.plot(x, y_obstacle_center_multiplier_2, label="C = (W/2, 2*R)")
# plt.plot(x, y_obstacle_center_multiplier_3, label="C = (W/2, 3*R)")
# plt.plot(x, y_obstacle_center_multiplier_4, label="C = (W/2, 4*R)")
# plt.plot(x, y_obstacle_center_multiplier_5, label="C = (W/2, 5*R)")

# plt.plot(x, y_obstacle_left_multiplier_2, label="C = (W/2 - R, 2*R)")
# plt.plot(x, y_obstacle_left_multiplier_3, label="C = (W/2 - R, 3*R)")

# plt.plot(x, y_obstacle_right_multiplier_2, label="C = (W/2 + R, 2*R)")
# plt.plot(x, y_obstacle_right_multiplier_3, label="C = (W/2 + R, 3*R)")

# plt.plot(x, y_obstacle_right_right_multiplier_2, label="C = (W/2 + 2*R, 2*R)")
# plt.plot(x, y_obstacle_right_right_multiplier_3, label="C = (W/2 + 2*R, 3*R)")

# plt.errorbar(x, y_obstacle_right_right_multiplier_3, yerr=errors, fmt='.')
## END INDIVIDUAL WITH ERRORS


## GROUP
# plt.scatter(x, y_no_obstacle, label="No obstacle", marker="o", color="green")

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
plt.xlim(xmin=0)
# plt.ylim(ymin=0, ymax=1100)
plt.ylim(ymin=0)
plt.tick_params(axis='both', which='major', labelsize=FONTSIZE)
plt.xlabel('Time [s]', fontsize=FONTSIZE)
plt.ylabel('Flow [1/s]', fontsize=FONTSIZE)
plt.legend(loc="lower right", fontsize=FONTSIZE)

plt.show()
