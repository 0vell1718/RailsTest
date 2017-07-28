from Framework.Request import Request


class BaseElement:
    def __init__(self, element_type):
        self._request = Request()
        self.__element_type = str(element_type)
        self.__element_id = ''

    def add(self, fields=None, field_element_id=None):
        if fields is None:
            fields = {}
        uri = '/index.php?/api/v2/add_' + self.__element_type + '/'
        if self.__element_type == 'case':
            uri += str(field_element_id)
        else:
            uri += '2'
            if field_element_id is not None and (self.__element_type == 'section' or self.__element_type == 'run'):
                fields.__setitem__('suite_id', field_element_id)
        response = self._request.send('POST', uri, fields)
        self.__element_id = str(response.json()['id'])
        return response.status_code

    def get_status_code(self):
        uri = '/index.php?/api/v2/get_' + self.__element_type + '/' + self.__element_id
        return self._request.send('GET', uri).status_code

    def get_fields(self, fields):
        uri = '/index.php?/api/v2/get_' + self.__element_type + '/' + self.__element_id
        response = self._request.send('GET', uri)
        data = {}
        for field in fields:
            data.__setitem__(field, response.json()[field])
        return data

    def delete(self):
        uri = '/index.php?/api/v2/delete_' + self.__element_type + '/' + self.__element_id
        return self._request.send('POST', uri).status_code

    def get_id(self):
        return self.__element_id
