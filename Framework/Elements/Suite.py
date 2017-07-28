from Framework.BaseElement import BaseElement
from Other.Random import Random


class Suite(BaseElement):
    def __init__(self):
        super().__init__('suite')


class Section(BaseElement):
    def __init__(self):
        super().__init__('section')


class Case(BaseElement):
    def __init__(self):
        super().__init__('case')

    def add_result(self, fields, run_id):
        uri = '/index.php?/api/v2/add_result_for_case/' + str(run_id) + '/' + self.get_id()
        response = self._request.send('POST', uri, fields)
        return response.status_code


class Run(BaseElement):
    def __init__(self):
        super().__init__('run')




random = Random()
# fields = random.set_random_fields('name', 'description')
# fields_keys = ('title', 'template_id', 'type_id', 'priority_id', 'refs')
# fields = random.set_random_fields(fields_keys)

suite = Suite()
section = Section()
case = Case()
run = Run()

suite_fields = ('name', 'description')
print(suite.add(random.set_random_fields(suite_fields)))
print(suite.get_status_code())

section_fields = ('name', 'description')
print(section.add(random.set_random_fields(section_fields), suite.get_id()))
print(section.get_status_code())

case_fields = ('title', 'template_id', 'type_id', 'priority_id', 'refs')
fields = random.set_random_fields(case_fields)

print(case.add(fields, section.get_id()))
print(case.get_status_code())
case_read_fields = case.get_fields(case_fields)

if fields == case_read_fields:
    print(True)

run_fields = ('name', 'description')
print(run.add(random.set_random_fields(run_fields), suite.get_id()))
print(run.get_status_code())

result_fields = ('status_id', 'comment', 'version', 'defects')
case.add_result(random.set_random_fields(result_fields), run.get_id())

print(case.delete())
print(run.delete())
print(section.delete())
print(suite.delete())
