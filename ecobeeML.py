import matplotlib.pyplot as plt
from sklearn import linear_model
from scipy import stats
import numpy as np

def getX():
	data = np.loadtxt('C:\\Users\\dhruv\\Desktop\\Code Projects - Dhruv Samdani\\EcobeeML\\ecobee_data\\WeatherTemp.txt',skiprows=0)
	return data

def getY():
	data = np.loadtxt('C:\\Users\\dhruv\\Desktop\\Code Projects - Dhruv Samdani\\EcobeeML\\ecobee_data\\EcobeeTemp.txt',skiprows=0)
	return data


x = getX()
y = getY()
degree = 1
slope, intercept, r_value, p_value, std_err = stats.linregress(x,y)
coeffs = np.polyfit(x,y,degree)[::-1]
equation = lambda a: sum([coeffs[i] * a**i for i in range(degree + 1)])


plt.plot(x, y, 'o', color='black')
plt.xlabel('Weather Temp')
plt.ylabel('Ecobee Temp')
plt.plot(x, equation(x))
plt.show()

# Prediction
outsideTemp =90 
print(equation(90))