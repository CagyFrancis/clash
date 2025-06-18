import os
import subprocess

def runClang(compiler:str, input_file:str, output_file:str):
    print(f'-- Building {input_file}', end=' ')
    args = [compiler, '-O0', '-emit-llvm', '-S', '-g', input_file, '-o', output_file]
    result = subprocess.run(args, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    if result.returncode != 0:
        print('-- fail')
        return True
    else:
        print('-- done')
        return False

def build(root_path:str, filename:str):
    input_file = os.path.join(root_path, filename)
    name, ext = os.path.splitext(filename)
    if ext == '.c':
        compiler = 'clang-15'
    elif ext == '.cpp':
        compiler = 'clang++-15'
    else:
        return
    output_file = os.path.join(root_path, f'{name}.ll')
    return runClang(compiler, input_file, output_file)

def buildExample(base_path:str):
    examples_path = os.path.join(base_path, 'examples')
    supports_path = os.path.join(base_path, 'supports')
    if os.path.exists(examples_path):
        list(map(lambda x: build(examples_path, x), os.listdir(examples_path)))
    if os.path.exists(supports_path):
        list(map(lambda x: build(supports_path, x), os.listdir(supports_path)))
    print(f'-> Compile Complete!')