import os
import re

directories = ["./drawable-%sdpi" % density for density in ["h", "m", "xh", "xxh"]]

for d in directories:
    for fname in os.listdir(d):
        legal_name = fname.replace("-", "_")
        try:
            # re.match matches at the beginning of the string.
            start_pos, end_pos = re.match("([0-9]+_+)", legal_name).span()
            legal_name = legal_name[end_pos:]
        except AttributeError:
            # When re.match returns None.
            pass

        os.rename(os.path.join(d, fname), os.path.join(d, legal_name))

