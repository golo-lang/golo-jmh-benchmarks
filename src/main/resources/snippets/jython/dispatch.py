def dispatch(data):
    result = ""
    for item in data:
        result = result + item.__str__()
    return result
