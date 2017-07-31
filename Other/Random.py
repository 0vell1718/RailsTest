import random
import string


class Random:
    def __init__(self, size):
        self.__random_size = size
        self.__Template_Test_Case_Text_id = 3

    def __random_name(self, chars=string.digits):
        return 'Auto_' + ''.join(random.choice(chars) for _ in range(self.__random_size))

    def __random_id(self, chars=('1', '2', '3', '4')):
        return int(''.join((random.choice(chars))))

    def set(self, *args):
        fields = {}
        for field in args:
            if field.count('template_id'):
                fields.__setitem__(field, self.__Template_Test_Case_Text_id)
            elif field.count('id'):
                fields.__setitem__(field, self.__random_id())
            else:
                fields.__setitem__(field, self.__random_name())
        return fields
