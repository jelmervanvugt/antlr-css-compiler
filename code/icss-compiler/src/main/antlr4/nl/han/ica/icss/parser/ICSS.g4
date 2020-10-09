grammar ICSS;

//--- LEXER: ---

// IF support:
IF: 'if';
ELSE: 'else';
BOX_BRACKET_OPEN: '[';
BOX_BRACKET_CLOSE: ']';


//Literals
TRUE: 'TRUE';
FALSE: 'FALSE';
PIXELSIZE: [0-9]+ 'px';
PERCENTAGE: [0-9]+ '%';
SCALAR: [0-9]+;


//Color value takes precedence over id idents
COLOR: '#' [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f];

//Specific identifiers for id's and css classes
ID_IDENT: '#' [a-z0-9\-]+;
CLASS_IDENT: '.' [a-z0-9\-]+;

//General identifiers
LOWER_IDENT: [a-z] [a-z0-9\-]*;
CAPITAL_IDENT: [A-Z] [A-Za-z0-9_]*;

//All whitespace is skipped
WS: [ \t\r\n]+ -> skip;

//Generic
OPEN_BRACE: '{';
CLOSE_BRACE: '}';
SEMICOLON: ';';
COLON: ':';
PLUS: '+';
MIN: '-';
MUL: '*';
ASSIGNMENT_OPERATOR: ':=';

//--- PARSER: ---
stylesheet: variableAssignment* styleRule* EOF;

//Stylerule
styleRule: selector OPEN_BRACE declaration+ CLOSE_BRACE;
selector: tagSelector | classSelector | idSelector;
tagSelector: LOWER_IDENT;
classSelector: CLASS_IDENT;
idSelector: ID_IDENT;

//Variables
propertyName: LOWER_IDENT;
declaration: propertyName COLON expression+ SEMICOLON | ifClause | elseClause;

//Values
value: boolLiteral | colorLiteral | percentageLiteral | pixelLiteral | scalarLiteral | variableReference;
boolLiteral: TRUE | FALSE;
colorLiteral: COLOR;
percentageLiteral: PERCENTAGE;
pixelLiteral: PIXELSIZE;
scalarLiteral: SCALAR;

//Variable assignment
variableAssignment: variableReference ASSIGNMENT_OPERATOR expression+ SEMICOLON;
variableReference: CAPITAL_IDENT;

//Expression
expression: operator value operator | value operator value | value;
operator: PLUS | MIN | MUL;

//If clause
ifClause: IF BOX_BRACKET_OPEN variableReference BOX_BRACKET_CLOSE OPEN_BRACE declaration+ CLOSE_BRACE;
elseClause: ELSE OPEN_BRACE declaration+ CLOSE_BRACE;








