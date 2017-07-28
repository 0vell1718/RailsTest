import random
import string
from Other.Global import Random_name_size


class Random:
    def __random_name(self, chars=string.digits):
        return 'Auto_' + ''.join(random.choice(chars) for _ in range(Random_name_size))

    def __random_id(self, chars=('1', '2', '3', '4')):
        return int(''.join((random.choice(chars))))

    def set_random_fields(self, data=()):
        fields = {}
        for field in data:
            if field.count('template_id'):
                fields.__setitem__(field, 3)
            elif field.count('id'):
                fields.__setitem__(field, self.__random_id())
            else:
                fields.__setitem__(field, self.__random_name())
        return fields
