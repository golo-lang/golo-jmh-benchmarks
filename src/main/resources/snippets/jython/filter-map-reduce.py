def run(data):
    return reduce(lambda acc, next: acc + next,
        map(lambda x: x * 2,
            filter(lambda x: x % 2 == 0, data)), 0)
