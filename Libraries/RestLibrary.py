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

    def add_case(self, section_id, title, priority_id, estimate, refs, template_id, steps):
        uri = '/index.php?/api/v2/add_case/' + section_id
        data = {'title': title, 'priority_id': priority_id, 'estimate': estimate, 'refs': refs,
                'template_id': template_id, 'custom_steps_separated': test_steps}
        response = self.__request_post(uri, data)
        return response.status_code, response.json()['id']

    def get_case(self, case_id):
        uri = '/index.php?/api/v2/get_case/' + case_id
        response = self.__request_qet(uri)
        title = response.json()['title']
        priority = response.json()['priority_id']
        estimate = response.json()['estimate']
        refs = response.json()['refs']
        steps = response.json()['custom_steps_separated']
        return response.status_code, title, priority, estimate, refs, steps

    def add_run(self, suite_id, name, description):
        uri = '/index.php?/api/v2/add_run/2'
        data = {'suite_id': suite_id, 'name': name, 'description': description}
        response = self.__request_post(uri, data)
        return response.status_code, response.json()['id']

    def get_run(self, run_id):
        uri = '/index.php?/api/v2/get_run' + run_id
        response = self.__request_qet(uri)
        return response.status_code


rest = Rest('https://testrail.a1qa.com', 'm.kalyn', '123456')
# rest.get_suite('1780')
# rest.add_suite('Mikhail', 'ddd')
# rest.add_section('2122', 'section_1', 'asdas')
# rest.get_section('1513')
test_steps = [{'content': 'Step 1', 'expected': 'Expected Result 1'}]
# print(rest.add_case('1513', 'test case 4', 1, '5m', "RF-1", 2, test_steps))
# print(rest.get_case('1742'))
# print(rest.add_run('2122', 'Mikhail', 'other'))


