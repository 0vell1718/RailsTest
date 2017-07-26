import requests


# вызов методa сервиса /basic-auth/:user/:passwd
# передача аргументов user и password в get запрос
# возвращаем код ответа от сервера
def authenticate(host, user, password):
    url = str(host) + '/basic-auth/user/passwd'
    r = requests.get(url, auth=(user, password))
    return str(r.status_code)


# вызов методa сервиса /get
# передача аргументов key и value для формирования header
# передаем header в get запрос
# ищем значение по ключу(тем самым проверяя его существование)
# возвращаем значение ключа и код ответа от сервера
def header(host, key, value):
    head = {key: value}
    url = str(host) + '/get'
    r = requests.get(url, headers=head)
    var = r.json()["headers"][key]
    return var, str(r.status_code)


# вызов методa сервиса stream/:n
# передача аргумента count для формирования количества строк в ответе
# передаем get запрос
# считаем количество строк
# возвращаем количество строк и код ответа от сервера
def stream(host, count):
    url = str(host) + '/stream/'
    r = requests.get(url + count)
    var = str(r.text)
    return str(var.count('}\n')), str(r.status_code)
