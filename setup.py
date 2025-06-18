import os
import sys
from scripts.parse import buildResult
from scripts.diff import buildPatch
from scripts.refine import buildRefine
from scripts.patch import patch, unpatch
from scripts.example import buildExample
from scripts.benchmark import buildBenchmark

arglen = len(sys.argv)
if arglen != 2:
   raise Exception(f'Expect ONE Argument but Got {arglen-1}')
mode = sys.argv[1]
base_path = os.path.dirname(__file__)
if mode == 'example':
    buildExample(base_path)
elif mode == 'patch':
    patch(base_path)
elif mode == 'unpatch':
    unpatch(base_path)
elif mode == 'build':
    buildBenchmark(base_path)
elif mode == 'refine':
    buildRefine(base_path)
elif mode == 'diff':
    buildPatch(base_path)
elif mode == 'parse':
    buildResult(base_path)
else:
    raise Exception(f'Expect [example, patch, unpatch, build, refine, diff, parse] but Got {mode}')