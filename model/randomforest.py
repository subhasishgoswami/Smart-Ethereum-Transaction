import pandas as pd
import numpy as np

features = pd.read_csv('file.csv', engine= "python")
pd.options.display.float_format = '{:.10f}'.format
features = features.replace(r'^\s*$', np.nan, regex=True)
features = features.fillna(method='ffill')
features.drop(features[features['appearHour'] == 10000].index, inplace = True)
features.head()
features.drop(features[features['wt(secs)'] == 'Error'].index, inplace = True)
features.head()
features['wt(secs)']= features['wt(secs)'].astype(float)
features['wt(secs)']= np.where(features['wt(secs)'].between(0,29),0,features['wt(secs)'])
features['wt(secs)']= np.where(features['wt(secs)'].between(30,59),1,features['wt(secs)'])
features['wt(secs)']= np.where(features['wt(secs)'].between(60,119),2,features['wt(secs)'])
features['wt(secs)']= np.where(features['wt(secs)'].between(120,3599),3,features['wt(secs)'])
features['wt(secs)']= np.where(features['wt(secs)'].between(3600,1000000000),4,features['wt(secs)'])

print(np.count_nonzero(features['wt(secs)']==0))
print(np.count_nonzero(features['wt(secs)']==1))
print(np.count_nonzero(features['wt(secs)']==2))
print(np.count_nonzero(features['wt(secs)']==3))
print(np.count_nonzero(features['wt(secs)']==4))
features= features.drop(['status','ISOTimeAppear','ISOBlockTime','appearEpochTimestamp','blockTimestamp','tx_hash', 'block', 'last40', 'last60', 'last540', 'last240', 'last300', 'last360', 'last480'], axis=1)
features['value(Eth)']= features['value(Eth)'].astype(float)
labels = np.array(features['wt(secs)'])
features= features.drop(['wt(secs)'], axis=1)
features.head()
feature_list = list(features.columns)
features = np.array(features)
from imblearn.over_sampling import SMOTE
from imblearn.combine import SMOTETomek
smt = SMOTETomek(random_state=42)
features, labels = smt.fit_resample(features, labels)
print((np.unique(labels, return_counts=True)))
from sklearn.model_selection import train_test_split

train_features, test_features, train_labels, test_labels = train_test_split(features, labels, test_size = 0.20,
                                                                           random_state = 42)
print('Training Features Shape:', train_features.shape)
print('Training Labels Shape:', train_labels.shape)
print('Testing Features Shape:', test_features.shape)
print('Testing Labels Shape:', test_labels.shape)
from sklearn.ensemble import RandomForestClassifier
rf= RandomForestClassifier(min_samples_split= 2, n_estimators= 250, random_state= 100, bootstrap= True, n_jobs=5)
rf.fit(train_features, train_labels)
import sklearn
predictions = rf.predict(test_features)
print(sklearn.metrics.accuracy_score(test_labels, predictions))
# Get numerical feature importances
importances = list(rf.feature_importances_)

# List of tuples with variable and importance
feature_importances = [(feature, round(importance, 2)) for feature, importance in zip(feature_list, importances)]

# Sort the feature importances by most important first
feature_importances = sorted(feature_importances, key = lambda x: x[1], reverse = True)

# Print out the feature and importances 
[print('Variable: {:20} Importance: {}'.format(*pair)) for pair in feature_importances]
from matplotlib import pyplot as plt
x_values = list(range(len(importances)))
plt.bar(x_values, importances, orientation = 'vertical')
plt.xticks(x_values, feature_list, rotation='vertical')
plt.ylabel('Importance'); plt.xlabel('Variable'); plt.title('Variable Importances')
serial_number= [serial_number for serial_number in range(270779)]
true_data = pd.DataFrame(data = {'serial_number': serial_number, 'actual': test_labels})
plt.plot(true_data['serial_number'], true_data['actual'], 'b-', label = 'actual')
predictions_data = pd.DataFrame(data = {'date': [serial_number for serial_number in range(270779)], 'prediction': predictions})
plt.plot(predictions_data['date'], predictions_data['prediction'], 'ro', label = 'prediction')
plt.xticks(rotation = '60'); 
plt.legend()

plt.xlabel('Serial Number'); plt.ylabel('Predicted Value'); plt.title('Actual and Predicted Values');
plt.hist(true_data['actual'], bins= 10, label= 'Actual')
plt.hist(predictions_data['prediction'], bins= 10, alpha= 0.8, label= 'Predicted')
from sklearn.metrics import f1_score
y_true = true_data['actual']
y_pred = predictions_data['prediction']
print(f1_score(y_true, y_pred, average='macro'))
print(f1_score(y_true, y_pred, average='micro'))
from sklearn.metrics import confusion_matrix
import seaborn as sn
data = confusion_matrix(y_true, y_pred)
df_cm = pd.DataFrame(data, columns=np.unique(y_true), index = np.unique(y_true))
df_cm.index.name = 'Actual'
df_cm.columns.name = 'Predicted'
plt.figure(figsize = (10,7))
sn.set(font_scale=1.4)#for label size
sn.heatmap(df_cm, cmap="Blues", annot=True,annot_kws={"size": 16}, fmt= 'g')# font size

