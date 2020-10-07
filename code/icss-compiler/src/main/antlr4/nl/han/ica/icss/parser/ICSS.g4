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
stylesheet: variableDeclaration* stylerule* EOF;

//General
name: LOWER_IDENT | CAPITAL_IDENT;
expressionType: PIXELSIZE | PERCENTAGE | COLOR | SCALAR | TRUE | FALSE | name;
operationType: PLUS | MIN | MUL;

//Stylerule
styleruleIdentifier: (ID_IDENT | CLASS_IDENT)? | name;
styleruleContent: (variableReference | ifClause)*;
stylerule: styleruleIdentifier OPEN_BRACE styleruleContent CLOSE_BRACE;

//Variables
variableDeclaration: name ASSIGNMENT_OPERATOR expressionType SEMICOLON;
variableReference: name COLON expressionType (operationType expressionType)* SEMICOLON;

//If else statement

ifClause: IF BOX_BRACKET_OPEN name BOX_BRACKET_CLOSE OPEN_BRACE ifClauseContent CLOSE_BRACE (ELSE OPEN_BRACE ifClauseContent CLOSE_BRACE)?;
ifClauseContent: (variableReference | ifClause)*;








