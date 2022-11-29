import json
from json import JSONEncoder
import numpy
import numpy as np

def read_integer_vectors(file_name):
    np_array = np.fromfile(file_name, dtype='int32')
    dimension = np_array[0]
    return np_array.reshape(-1, dimension + 1)[:, 1:].copy()

def read_float_vectors(file_name):
    return read_integer_vectors(file_name).view('float32')

class NumpyArrayEncoder(JSONEncoder):
    def default(self, obj):
        if isinstance(obj, numpy.ndarray):
            return obj.tolist()
        return JSONEncoder.default(self, obj)


print('Dumping Ground Truth')
with open('ground_truth_vectors.json', 'w') as f:
    f.write(json.dumps(read_integer_vectors('siftsmall_groundtruth.ivecs'), cls=NumpyArrayEncoder))

print('Dumping Query')
with open('query_vectors.json', 'w') as f:
    f.write(json.dumps(read_float_vectors('siftsmall_query.fvecs'), cls=NumpyArrayEncoder))

print('Dumping Base Vectors')
with open('base_vectors.json', 'w') as f:
    f.write(json.dumps(read_float_vectors('siftsmall_base.fvecs'), cls=NumpyArrayEncoder))

print('KNN')
with open('knn.json', 'w') as f:
    f.write(json.dumps(read_integer_vectors('sift_small.knn'), cls=NumpyArrayEncoder))