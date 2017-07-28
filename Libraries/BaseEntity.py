import base64
import json
import string
import random
from random import randrange

import requests

from Libraries.Request import Request


class BaseEntity(Request):
    def __init__(self, base_url, user, password):
        super().__init__(base_url, user, password)
        self.__entity_id = ''

    @staticmethod
    def __random_string(size=6, chars=string.ascii_uppercase + string.digits):
        return ''.join(random.choice(chars) for _ in range(size))

    @staticmethod
    def __get_json_attribute_value(response, key):
        return str(response.json()[str(key)])



    def get_entity_status_code(self, entity, entity_id):
        uri = '/index.php?/api/v2/get_' + str(entity) + '/' + str(entity_id)
        return self.__send_request('GET', uri).status_code

    def get_entity_fields(self, entity, entity_id, fields):
        uri = '/index.php?/api/v2/get_' + str(entity) + '/' + str(entity_id)
        response = self.__send_request('GET', uri)
        data = {}
        for field in fields:
            data.__setitem__(field, self.__get_json_attribute_value(response, field))
        return data

    def delete_entity(self, entity, entity_id):
        uri = '/index.php?/api/v2/delete_' + str(entity) + '/' + str(entity_id)
        self.__send_request('POST', uri)

    def get_case(self, case_id):
        uri = '/index.php?/api/v2/get_case/' + case_id
        response = self.__send_request('GET', uri)
        title = response.json()['title']
        priority = response.json()['priority_id']
        estimate = response.json()['estimate']
        refs = response.json()['refs']
        steps = response.json()['custom_steps_separated']
        return response.status_code, title, priority, estimate, refs, steps

    def create_random_fields(self, fields):
        return {}

    def add_entity(self, entity, fields, id=None):
        uri = '/index.php?/api/v2/add_' + entity + '/'
        if entity == 'case':
            uri += str(id)
        else:
            uri += '2'

        response = self.__send_request('POST', uri, fields)
        self.__entity_id = self.__get_json_attribute_value(response, 'id')

    def add_suite(self, name, description):
        uri = '/index.php?/api/v2/add_suite/2'
        data = {'name': name, 'description': description}
        response = self.__send_request('POST', uri, data)
        return response.status_code, response.json()['id']

    def add_section(self, suite_id, name, description):
        uri = '/index.php?/api/v2/add_section/2'
        data = {'suite_id': suite_id, 'name': name, 'description': description}
        response = self.__send_request('POST', uri, data)
        return response.status_code, response.json()['id']

    def add_run(self, suite_id, name, description):
        uri = '/index.php?/api/v2/add_run/2'
        data = {'suite_id': suite_id, 'name': name, 'description': description}
        response = self.__send_request('POST', uri, data)
        return response.status_code, response.json()['id']

    def add_case(self, section_id, title, template_id, type_id, priority_id, refs, steps):
        uri = '/index.php?/api/v2/add_case/' + section_id
        data = {'title': title, 'template_id': template_id, 'type_id': type_id,
                'priority_id': priority_id, 'refs': refs, 'custom_steps_separated': steps}
        response = self.__send_request('POST', uri, data)
        return response.status_code, response.json()['id']

    def add_result_for_case(self, run_id, case_id):
        status_id = 5
        comment = self.__random_string()
        version = self.__random_string()
        defects = self.__random_string()
        uri = '/index.php?/api/v2/add_result_for_case/' + str(run_id) + '/' + str(case_id)
        data = {'status_id': status_id, 'comment': comment, 'version': version, 'defects': defects}
        response = self.__send_request('POST', uri, data)
        return response.status_code, response.text


rest = Rest('https://testrail.a1qa.com', 'm.kalyn', '123456')
# rest.get_suite('1780')
# print(rest.add_suite('Mikhail', 'ddd'))
# rest.add_section('2122', 'section_1', 'asdas')
# rest.get_section('1513')
test_steps = [{'content': 'Step 1', 'expected': 'Expected Result 1'}]
# print(rest.add_case('1513', 'test case 4', 1, '5m', "RF-1", 2, test_steps))
# print(rest.get_case('1742'))
# print(rest.add_run('2122', 'Mikhail', 'other'))
# print(rest.get_run(1073))
# print(rest.add_result_for_case(1073, 1742))
# print(rest.get_results_for_case(1073, 1742))

# print(rest.delete('suite', 2123))
print(rest.get_entity_status_code('case', 586))
print(rest.get_entity_status_code('case', 484))
print(rest.get_entity_fields('case', 597, {'title', 'refs', 'id'}))
