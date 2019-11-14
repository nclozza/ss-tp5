import matplotlib.pyplot as plt
import math

path_0 = '../../plot/No obstacle/energy; no static particle, i = 0.txt'
path_1 = '../../plot/No obstacle/energy; no static particle, i = 1.txt'
path_2 = '../../plot/No obstacle/energy; no static particle, i = 2.txt'

file_0 = open(path_0,'r')
file_1 = open(path_1,'r')
file_2 = open(path_2,'r')

array_0 = file_0.read().split('\n')
array_1 = file_1.read().split('\n')
array_2 = file_2.read().split('\n')

list_0 = list(map(lambda x: x.split(","), array_0))
list_1 = list(map(lambda x: x.split(","), array_1))
list_2 = list(map(lambda x: x.split(","), array_2))

file_0.close()
file_1.close()
file_2.close()

final_0 = []
final_1 = []
final_2 = []
x_array = []

for i in range(0, len(list_0)):
  final_0.append(float(list_0[i][1]))
  final_1.append(float(list_1[i][1]))
  final_2.append(float(list_2[i][1]))
  x_array.append(float(list_0[i][0]))

prom = []
x = []
errors = []

for i in range(0, len(final_0)):
  prom.append((final_0[i] + final_1[i] + final_2[i]) / 3)
  x.append(x_array[i])
  errors.append(math.sqrt(((final_0[i] - prom[i])**2 + (final_1[i] - prom[i])**2 + (final_2[i] - prom[i])**2 / 2)))

for i in range(0, len(prom)):
  print(str(x[i]) + "," + str(prom[i]))
  # print(str(x[i]) + "," + str(errors[i]))

plt.plot(x, prom, label="")
plt.errorbar(x, prom, yerr=errors, fmt='.')

FONTSIZE = 20
# plt.legend(loc='top right', fontsize=FONTSIZE)
plt.ylabel('Average velocity [m/s]', fontsize=FONTSIZE)
plt.xlabel('x [1/m²]', fontsize=FONTSIZE)
plt.tick_params(axis='both', which='major', labelsize=FONTSIZE)

# plt.show()