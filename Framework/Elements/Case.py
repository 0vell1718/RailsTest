from Framework.BaseElement import BaseElement


class Case(BaseElement):
    def __init__(self):
        super().__init__('case')

    def add_result(self, fields, run_id):
        uri = '/index.php?/api/v2/add_result_for_case/' + str(run_id) + '/' + self.get_id()
        response = self._request.send('POST', uri, fields)
        return response.status_code
