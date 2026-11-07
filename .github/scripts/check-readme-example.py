"""Compile the README program and compare its observable results."""
import os
from pathlib import Path
import re
import subprocess
import sys

blocks = re.findall(r'```java\n(.*?)```', Path('README.md').read_text(encoding='utf-8'), re.S)
assert len(blocks) == 1
work = Path('build/readme-example').resolve()
work.mkdir(parents=True, exist_ok=True)
source = work / 'QuantityExample.java'
source.write_text(blocks[0], encoding='utf-8')
subprocess.run([str(Path(os.environ['JAVA_HOME']) / 'bin/javac'), '--release', '11', '-encoding', 'UTF-8', '-cp', sys.argv[1], '-d', str(work), str(source)], check=True)
runtime = Path(os.environ.get('TEST_JAVA_HOME', os.environ['JAVA_HOME'])) / 'bin/java'
result = subprocess.check_output([str(runtime), '-cp', str(work) + os.pathsep + sys.argv[1], 'QuantityExample'], text=True)
assert result.splitlines() == ['Optional[42]', 'Optional[invalid: abc]', '[quantity=42, invalid: abc]'], result
print('README example: ' + result.strip().replace('\n', '; '))
