import matplotlib.pyplot as plt
import csv
import math


def f(z):
    return 57692.3 * math.pow(z, 2) - 646154 * z + 1.81292 * math.pow(10, 6)

# x = [0.007, 0.01, 0.012, 0.014, 0.017]
# y = [20.64, 20.625, 20.62, 20.624, 20.645]


x = list(map(lambda z: z/1000, list(range(5400, 5800, 1))))
y = list(map(f, x))

plt.plot(x, y, label='ECM')

plt.plot(5.601, f(5.601), 'go', label="Minimum error")
plt.xlim(xmin=5.47, xmax=5.765)
# plt.ylim(ymin=20.5, ymax=20.7)

plt.xlabel('Parameter C', fontsize=20)
plt.ylabel('Error', fontsize=20)
plt.legend(loc="upper right", fontsize=20)

plt.tick_params(axis='both', which='major', labelsize=20)
plt.show()
