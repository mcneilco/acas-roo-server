-- Name: convert input integer to license plate format
-- dependencies -- install language plperl

--Do this outside of Flyway (be careful with the cascade drop
--DROP EXTENSION IF EXISTS plperl CASCADE;
--CREATE EXTENSION plperl;


CREATE OR REPLACE FUNCTION licenseplate(integer)

RETURNS text

LANGUAGE plperl

IMMUTABLE STRICT

AS $function$

  my ($int)=@_; $int--;

  my $num;

  if ($int >= 1062600) {

    ## changed algorithm to skip '000'

    $int -= 1063;

    $num=$int%999 + 1;

    $int=int($int/999);

  } else {

    $num=$int%1000;

    $int=int($int/1000);

  };

  my $let='';

  foreach (1..3) {

    $let.=chr(ord("A")+$int%26);

    $int= int ($int/26);

  };

  die "overflow $_[0]" if $int>0;

  return sprintf "%s%03d", scalar reverse($let),$num;

$function$