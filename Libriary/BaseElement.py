import base64
import json
import requests


class BaseElement:
    def __init__(self, element_type, host, user, password):
        self.__element_type = str(element_type)
        self.__element_id = ''
        self.__url = host
        self.__user = user
        self.__password = password
        self.__auth = self.__get_basic_authorization_credentials(self.__user, self.__password)
        self.__header = {'Content-Type': 'application/json', 'Authorization': self.__auth}

    @staticmethod
    def __get_basic_authorization_credentials(user, password):
        return 'Basic ' + str(base64.b64encode(bytes('%s:%s' % (user, password), 'utf-8')), 'ascii').strip()

    def __send(self, method, uri, data=None):
        if data is None:
            data = {}
        if method == 'GET':
            return requests.get(self.__url + uri, headers=self.__header)
        elif method == 'POST':
            return requests.post(self.__url + uri, headers=self.__header, data=bytes(json.dumps(data), 'utf-8'))

    def add(self, field_element_id, fields=None):
        if fields is None:
            fields = {}
        uri = '/index.php?/api/v2/add_' + self.__element_type + '/'
        if self.__element_type == 'case':
            uri += str(field_element_id)
        else:
            uri += '2'
            if field_element_id is not None and (self.__element_type == 'section' or self.__element_type == 'run'):
                fields.__setitem__('suite_id', field_element_id)
        response = self.__send('POST', uri, fields)
        self.__element_id = str(response.json()['id'])

    def get_status_code(self):
        uri = '/index.php?/api/v2/get_' + self.__element_type + '/' + self.__element_id
        return self.__send('GET', uri).status_code

    def get_fields(self, *args):
        uri = '/index.php?/api/v2/get_' + self.__element_type + '/' + self.__element_id
        response = self.__send('GET', uri)
        data = {}
        for field in args:
            data.__setitem__(field, response.json()[field])
        return data

    def delete(self):
        uri = '/index.php?/api/v2/delete_' + self.__element_type + '/' + self.__element_id
        self.__send('POST', uri)

    def get_id(self):
        return str(self.__element_id)

    def add_result(self, run_id, case_id, fields=None):
        uri = '/index.php?/api/v2/add_result_for_case/' + str(run_id) + '/' + str(case_id)
        self.__send('POST', uri, fields)
