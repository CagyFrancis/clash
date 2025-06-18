import os
import platform
import subprocess

def runBenchmark(benchmark:str):
    global bench_path, refined_path, patch_path, workspace
    src_path = os.path.join('benchmark', benchmark)
    dst_path = os.path.join('refined', benchmark)
    file = os.path.join(patch_path, f'{benchmark}.patch')
    if not os.path.isdir(src_path):
        return True
    print(f'-- Patching {benchmark}', end=' ')
    if not os.path.exists(dst_path):
        print('-- fail')
        return True
    with open(file, 'wt') as fs:
        args = ['diff', '-ruw', src_path, dst_path]
        subprocess.run(args, cwd=workspace, stdout=fs, stderr=subprocess.PIPE)
    print('-- done')
    return False

def buildPatch(base_path:str):
    global bench_path, refined_path, patch_path, workspace
    if platform.system() != 'Linux':
        raise Exception('Patch can only run in Linux system!')
    workspace = base_path
    bench_path = os.path.join(base_path, 'benchmark')
    refined_path = os.path.join(base_path, 'refined')
    patch_path = os.path.join(base_path, 'patch')
    if not os.path.exists(patch_path):
        os.makedirs(patch_path)
    if os.path.exists(bench_path):
        list(map(runBenchmark, os.listdir(bench_path)))