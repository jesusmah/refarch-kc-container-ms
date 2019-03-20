#!/usr/bin/env python
# coding: utf-8

# In[1]:


import pandas as pd
import numpy as np
from sklearn import linear_model
from sklearn import metrics


# In[6]:


data = pd.read_csv('../data/container_matrix_test.csv', delimiter=",")
data.head()


# In[7]:


# create a Python list of feature names
feature_cols = ['Temperature(celsius)', 'Door_Open','Target_Temperature(celsius)','Amp','ContentType','CumulativePowerConsumption'
                          ,'Humidity','CO2','Defrost_Cycle']

# use the list to select a subset of the original DataFrame
X = data[feature_cols]
y = data['Maintainence_Required']

# print the first 5 rows
X.head()


# In[8]:


import pickle

#loading a model from a file called model.pkl
model = pickle.load(open("model.pkl","r"))


# In[9]:


# make predictions on the input set
y_pred = model.predict(X)
print y_pred
#y_pred.shape
print(np.sqrt(metrics.mean_squared_error(y, y_pred)))


# In[ ]:





# In[ ]:





# In[ ]:



