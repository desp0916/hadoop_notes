#!/usr/bin/env perl
use POSIX qw(strftime);

while (<>) {
	@s = split(/,/, $_);
        ($sec,$min,$hour,$mday,$mon,$year,$wday,$yday,$isdst) = localtime($s[3]);
        $ts = sprintf("%04d-%02d-%02d %02d:%02d:%02d",
                                   $year+1900,$mon+1,$mday,$hour,$min,$sec);
	# Twice slower than above, so we don't use this.
        # $ts = strftime("%Y-%m-%d %H:%M:%S", localtime($s[3]));
	print $s[0] . "," . $s[1] . "," . $s[2] . "," . $ts . "\n";
}

