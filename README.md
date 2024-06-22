## Lexems table

| Terminal | Token |
| -------- | ----- |
| \|       | OR    |
| ^        | XOR   |
| &        | AND   |
| !        | NOT   |
| a        | VAR   |
| (        | LPAR  |
| )        | RPAR  |
| $        | END   |
| ->       | IMPL  |



## Grammar

S' ->  I

I -> S impl I

I -> S



S -> O | S

S -> O



O -> X ^ O

O -> X



X -> A & X

X -> A



A -> !A

A -> E



E -> (S)

E -> T



T -> x



### First/Follow

| Term | first | follow   |
| ---- | ----- | -------- |
| S'   | !(x   | $        |
| I    | !(x   | )$       |
| S    | !(x   | ->)$     |
| O    | !(x   | ->\|)$   |
| X    | !(x   | ->\|^)$  |
| A    | !(x   | ->\|^&)$ |
| E    | (x    | ->^&)$   |
| T    | x     | ->^&)$   |



