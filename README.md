# antlr-css-compiler

Jelmer van Vugt 

## Algemene eisen en MUSTs
<p>Het product voldoet aan alle algemene eisen zoals gedefinieerd in hoofdstuk 4.1 van de Assignment.md bestand van de ICSS-Compiler. Ook voldoet het aan alle MUST eisen.</p> 

## Keuze eisen
In de checker worden de volgende eisen gehandhaafd:
<ul>
  <li>CH01 - Controleren of er geen niet gedefinieerde variabelen worden gebruikt.</li>
  <li>CH02 - Controleren of de ExpressionTypes van beide kanten bij Add - en Subtract Operations gelijk zijn.</li>
  <li>CH03 - Controleren of er geen kleuren worden gebruikt binnen operaties.</li>
  <li>CH05 - Controleren of de ConditionalStatement van een IfClause van ExpressionType BooleanLiteral is.</li>
  <li>CH06 - Controleren of variabelen enkel binnen hun scope worden gebruikt.</li>
  </ul>
  
  ## Extra uitbreidingen
<p>Voor het afhandelen dat variabelen binnen hun scope worden gebruikt is een ScopeManager klasse geschreven. Deze ScopeManager bevat een LinkedList die op zijn beurt HashMaps. Omdat het controleren van variabelen binnen hun eigen scope zowel in de Checker als de Transformer moest worden afgehandeld zou dit zorgen voor veel dubbele code. De enige wijziging zou zijn dat de HashMaps in plaats van de datatypes String en ExpressionType, String en Literal zouden bevatten. Dit is opgelost door de ScopeManager generic te maken. Hierdoor kan dezelfde klasse worden gebruikt voor verschillende waarden van AnyType.</p>
