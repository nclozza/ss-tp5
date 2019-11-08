import matplotlib.pyplot as plt
import csv

x = []
y_D_015 = []
y_D_018 = []
y_D_022 = []
y_D_025 = []

with open('FALL_D015.csv', 'r') as csvfile:
    plots = csv.reader(csvfile, delimiter=',')
    for row in plots:
        x.append(float(row[0]))
        y_D_015.append(float(row[1]))

with open('FALL_D018.csv', 'r') as csvfile:
    plots = csv.reader(csvfile, delimiter=',')
    for row in plots:
        y_D_018.append(float(row[1]))

with open('FALL_D022.csv', 'r') as csvfile:
    plots = csv.reader(csvfile, delimiter=',')
    for row in plots:
        y_D_022.append(float(row[1]))

with open('FALL_D025.csv', 'r') as csvfile:
    plots = csv.reader(csvfile, delimiter=',')
    for row in plots:
        y_D_025.append(float(row[1]))

plt.plot(x, y_D_015, label="D = 0,15 m")
plt.plot(x, y_D_018, label="D = 0,18 m")
plt.plot(x, y_D_022, label="D = 0,22 m")
plt.plot(x, y_D_025, label="D = 0,25 m")

plt.xlim(xmin=0)
plt.ylim(ymin=0, ymax=1100)
plt.xlabel('Time [s]')
plt.ylabel('Flow [1/s]')
plt.legend(loc="upper right")

plt.show()
