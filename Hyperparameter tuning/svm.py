import pandas as pd
from imblearn.under_sampling import RandomUnderSampler
import numpy as np
from sklearn.svm import SVC
features = pd.read_csv('file.csv', engine= "python")
pd.options.display.float_format = '{:.10f}'.format
features = features.replace(r'^\s*$', np.nan, regex=True)
features = features.fillna(method='ffill')
features.drop(features[features['wt(secs)'] == 'unk'].index, inplace = True)
features.drop(features[features['wt(secs)'] == 'Error'].index, inplace = True)
features['wt(secs)']= features['wt(secs)'].astype(int)
features['wt(secs)']= np.where(features['wt(secs)'].between(0,29),0,features['wt(secs)'])
features['wt(secs)']= np.where(features['wt(secs)'].between(30,59),1,features['wt(secs)'])
features['wt(secs)']= np.where(features['wt(secs)'].between(60,119),2,features['wt(secs)'])
features['wt(secs)']= np.where(features['wt(secs)'].between(120,3599),3,features['wt(secs)'])
features['wt(secs)']= np.where(features['wt(secs)'].between(3600,1000000000),4,features['wt(secs)'])
features= features.drop(['tx_hash', 'block', 'last40', 'last60', 'last540', 'last240', 'last300', 'last360', 'last480'], axis=1)
features['value(Eth)']= features['value(Eth)'].astype(float)
labels = np.array(features['wt(secs)'])
features= features.drop('wt(secs)', axis = 1)
features['appearHour']= np.where(features['appearHour']== 10000,24,features['appearHour'])
feature_list = list(features.columns)
features = np.array(features)
rus = RandomUnderSampler()
# resampling X, y
features, labels = rus.fit_resample(features, labels)
from sklearn.model_selection import train_test_split

train_features, test_features, train_labels, test_labels = train_test_split(features, labels, test_size = 0.20,
                                                                           random_state = 42)
from sklearn.model_selection import GridSearchCV
parameters = [{
'C': [0.5,1,2],
'kernel': ['linear', 'poly', 'rbf'],
'degree': [3, 5],
'gamma': ['scale', 'auto'],
'shrinking': [True, False],
'probability': [True, False],
'cache_size': [200,500],
'break_ties': [True, False],
'random_state' : [100]
}]
clf = GridSearchCV(
        SVC(), parameters, scoring='accuracy', n_jobs= 10
)
clf.fit(train_features, train_labels)
print(clf.best_params_)
