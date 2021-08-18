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
from sklearn.neural_network import MLPClassifier
model_mlp = MLPClassifier(hidden_layer_sizes= (100,100, 2))
model_mlp.fit(train_features, train_labels)
predictions_mlp = model_mlp.predict(test_features)
import sklearn
print(sklearn.metrics.accuracy_score(test_labels, predictions_mlp)*100, '%')