import base64
import json
import requests


class Rest:
    def __init__(self, base_url, user, password):
        self.user = user
        self.password = password
        self.__url = base_url
        self.__auth = str(base64.b64encode(bytes('%s:%s' % (self.user, self.password), 'utf-8')), 'ascii').strip()
        self.__header = {'Content-Type': 'application/json', 'Authorization': 'Basic ' + self.__auth}

    def __request_qet(self, uri):
        return requests.get(self.__url + uri, headers=self.__header)

    def __request_post(self, uri, data):
        return requests.post(self.__url + uri,
                             headers=self.__header,
                             data=bytes(json.dumps(data), 'utf-8')
                             )

    def get_suite(self, suite_id):
        uri = '/index.php?/api/v2/get_suite/' + suite_id
        response = self.__request_qet(uri)
        return response.status_code

    def add_suite(self, name, description):
        uri = '/index.php?/api/v2/add_suite/2'
        data = {'name': name, 'description': description}
        response = self.__request_post(uri, data)
        return response.status_code, response.json()['id']

    def add_section(self, suite_id, name, description):
        uri = '/index.php?/api/v2/add_section/2'
        data = {'suite_id': suite_id, 'name': name, 'description': description}
        response = self.__request_post(uri, data)
        return response.status_code, response.json()['id']

    def get_section(self, section_id):
        uri = '/index.php?/api/v2/get_section/' + section_id
        response = self.__request_qet(uri)
        return response.status_code



rest = Rest('https://testrail.a1qa.com', 'm.kalyn', '123456')
# rest.get_suite('1780')
# rest.add_suite('Mikhail', 'ddd')
# rest.add_section('2122', 'section_1', 'asdas')
# rest.get_section('1513')
