import matplotlib.pyplot as plt
import csv

y_D_015 = []
y_D_018 = []
y_D_022 = []
y_D_025 = []

with open('FALL_D015.csv', 'r') as csvfile:
    plots = csv.reader(csvfile, delimiter=',')
    for row in plots:
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


def avg(ls):
    return sum(ls)/len(ls)


x = [0.15, 0.18, 0.22, 0.25]
y = [avg(y_D_015),
     avg(y_D_018),
     avg(y_D_022),
     avg(y_D_025)]


def regression(z):
    return 6262.05323 * z + (-618.50074)


print(y)
plt.scatter(x, y, label="Flow")  # , label="D = 0,15 m")


def app(ls1, ls2):
    ls1.append(ls2)
    return ls1


print(list(map(regression, x)),)
plt.plot([0.14] + x + [0.27],
         list(map(regression, [0.14] + x + [0.27])), label="C = 5.61", color='g')

# plt.xlim(xmin=0)
# plt.ylim(ymin=0, ymax=1100)
plt.ylabel('Flow [1/s]', fontsize=20)
plt.xlabel('Aperture [m]', fontsize=20)
plt.legend(loc="lower right", fontsize=20)
plt.tick_params(axis='both', which='major', labelsize=20)
plt.show()
