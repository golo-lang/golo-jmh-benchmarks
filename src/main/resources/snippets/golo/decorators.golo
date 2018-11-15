module Decorators

function times = |n| -> |f| -> |a, b| {
  let r = f(a, b)
  return r * n
}

@times(123)
function add = |v, n| -> v + n
