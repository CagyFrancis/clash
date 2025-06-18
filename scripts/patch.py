import os
import platform
import subprocess

def unpatchBenchmark(benchmark:str):
    global bench_path, patch_path
    print(f'-- Unpatching {benchmark}', end=' ')
    file = os.path.join(patch_path, f'{benchmark}.patch')
    if not os.path.exists(file):
        print('-- fail')
        return True
    with open(file, 'rt') as fs:
        args = ['patch', '-d', benchmark, '-Rp2']
        subprocess.run(args, cwd=bench_path, stdin=fs, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    print('-- done')
    return False

def patchBenchmark(benchmark:str):
    global bench_path, patch_path
    print(f'-- Patching {benchmark}', end=' ')
    file = os.path.join(patch_path, f'{benchmark}.patch')
    if not os.path.exists(file):
        print('-- fail')
        return True
    with open(file, 'rt') as fs:
        args = ['patch', '-d', benchmark, '-p2']
        subprocess.run(args, cwd=bench_path, stdin=fs, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    print('-- done')
    return False

def patch(base_path:str):
    global bench_path, patch_path
    if platform.system() != 'Linux':
        raise Exception('Patch can only run in Linux system!')
    bench_path = os.path.join(base_path, 'benchmark')
    patch_path = os.path.join(base_path, 'patch')
    if os.path.exists(bench_path):
        list(map(patchBenchmark, os.listdir(bench_path)))

def unpatch(base_path:str):
    global bench_path, patch_path
    if platform.system() != 'Linux':
        raise Exception('Patch can only run in Linux system!')
    bench_path = os.path.join(base_path, 'benchmark')
    patch_path = os.path.join(base_path, 'patch')
    if os.path.exists(bench_path):
        list(map(unpatchBenchmark, os.listdir(bench_path)))