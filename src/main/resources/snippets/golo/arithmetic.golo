module Arithmetics

function gcd = |x, y| {
  var a = x
  var b = y
  while a != b {
    if a > b {
      a = a - b
    } else {
      b = b - a
    }
  }
  return a
}

function sum = |x, y| -> x + y

function sum_with_constant = |x| -> x + 10_L

function sum_of_constants = -> 22_L + 10_L
