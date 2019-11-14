import matplotlib.pyplot as plt
import math
import numpy as np 

## CALCULATE QT

def calculate_qt(times):
  quantity = 19 # // 19 + la inicial = 20
  end = len(times) - quantity - 1

  i = 0
  array = []

  while i <= end:
    if times[i] >= 3:
      array.append(times[i + quantity] - times[i])

    i += 1

  sum = 0
  for value in array:
    sum += value

  seconds_prom = sum / len(array)
  Qt = (quantity + 1) / seconds_prom

  return Qt

def calculate_prom(paths):
  # path_0 = '../../plot/No obstacle/fallen_particles no static particle, i = 0.txt'
  # path_1 = '../../plot/No obstacle/fallen_particles no static particle, i = 1.txt'
  # path_2 = '../../plot/No obstacle/fallen_particles no static particle, i = 2.txt'

  file_0 = open(paths[0],'r')
  file_1 = open(paths[1],'r')
  file_2 = open(paths[2],'r')

  array_0 = file_0.read().split('\n')
  array_1 = file_1.read().split('\n')
  array_2 = file_2.read().split('\n')

  file_0.close()
  file_1.close()
  file_2.close()

  array_0 = array_0[: len(array_0) - 1]
  array_1 = array_1[: len(array_1) - 1]
  array_2 = array_2[: len(array_2) - 1]

  float_array_0 = []
  float_array_1 = []
  float_array_2 = []

  for value in array_0:
    float_array_0.append(float(value))

  for value in array_1:
    float_array_1.append(float(value))

  for value in array_2:
    float_array_2.append(float(value))

  value_0 = calculate_qt(float_array_0)
  value_1 = calculate_qt(float_array_1)
  value_2 = calculate_qt(float_array_2)

  prom = (value_0 + value_1 + value_2) / 3

  return prom

## END CALCULATE QT


## BEVERLOO

g = 9.8
n_particles = 170
diameter = 0.15
r = 0.0125
w = 0.4
l = 1.5 / 4 # puede que acá esté el error, no sé si hay que considerar L como el colchón de partículas

def beverloo(d, c):
  return (n_particles / (l * w)) * math.sqrt(g) * (d - c*r)**1.5

## END BEVERLOO

def quadratic_error(y_i, real):
  return (y_i - real)**2

best_c = -1
less_quadratic_error = math.inf
sum_quadratic_errors = 0

array_c = []
array_quadratic_error = []

paths_15 = [
  '../../plot/No obstacle/fallen_particles no static particle, i = 0.txt',
  '../../plot/No obstacle/fallen_particles no static particle, i = 1.txt',
  '../../plot/No obstacle/fallen_particles no static particle, i = 2.txt'
]

paths_18 = [
  '../../plot/No obstacle/fallen_particles no static particle, i = 0.txt',
  '../../plot/No obstacle/fallen_particles no static particle, i = 1.txt',
  '../../plot/No obstacle/fallen_particles no static particle, i = 2.txt'
]

paths_22 = [
  '../../plot/No obstacle/fallen_particles no static particle, i = 0.txt',
  '../../plot/No obstacle/fallen_particles no static particle, i = 1.txt',
  '../../plot/No obstacle/fallen_particles no static particle, i = 2.txt'
]

for i in np.arange(6.5, 7.5, 0.001):
  sum_quadratic_errors = 0
  sum_quadratic_errors += quadratic_error(calculate_prom(paths_15), beverloo(0.15, i))
  sum_quadratic_errors += quadratic_error(calculate_prom(paths_18), beverloo(0.18, i))
  sum_quadratic_errors += quadratic_error(calculate_prom(paths_22), beverloo(0.22, i))
  array_c.append(i)

  array_quadratic_error.append(sum_quadratic_errors)
  
  if less_quadratic_error > sum_quadratic_errors:
    less_quadratic_error = sum_quadratic_errors
    best_c = i


print(best_c)
print(less_quadratic_error)

plt.plot(array_c, array_quadratic_error, label="")
plt.plot(best_c, less_quadratic_error, marker="o")

FONTSIZE = 20
# plt.legend(loc='top right', fontsize=FONTSIZE)
plt.ylabel('Error', fontsize=FONTSIZE)
plt.xlabel('Parameter c', fontsize=FONTSIZE)
plt.tick_params(axis='both', which='major', labelsize=FONTSIZE)

plt.show()