import os
import re
import datetime
import subprocess

def setTask(key:str, name:str, compiler:str, src_file:str, mid_path:str, dst_path:str, worklist:dict[str, tuple[str|list]]):
    mid_file = os.path.join(mid_path, f'{name}.ll')
    dst_file = os.path.join(dst_path, f'{key}.ll')
    task = worklist.get(key, (compiler, [], [], dst_file))
    task[1].append(src_file)
    task[2].append(mid_file)
    worklist[key] = task

def setWorklist(filename:str, src_path:str, mid_path:str, dst_path:str, worklist:dict[str, tuple[str|list]]):
    name, ext = os.path.splitext(filename)
    if ext == '.c':
        compiler = 'clang'
    elif ext == '.cpp':
        compiler = 'clang++'
    else:
        return
    src_file = os.path.join(src_path, filename)
    if name[-2:].isdigit():
        setTask(name, name, compiler, src_file, mid_path, dst_path, worklist)
    elif name.endswith('_goodB2G') or name.endswith('_goodG2B'):
        setTask(name[:-8], name, compiler, src_file, mid_path, dst_path, worklist)
    elif name.endswith('_bad'):
        setTask(name[:-4], name, compiler, src_file, mid_path, dst_path, worklist)
    elif name.endswith('_good1'):
        setTask(name[:-6], name, compiler, src_file, mid_path, dst_path, worklist)
    else:
        setTask(name[:-1], name, compiler, src_file, mid_path, dst_path, worklist)

def getSuffix(filename:str):
    has_fopen = re.search(r'fopen', filename, re.IGNORECASE)
    has_class = re.search(r'class', filename, re.IGNORECASE)
    has_struct = re.search(r'struct', filename, re.IGNORECASE)
    has_array = re.search(r'array', filename, re.IGNORECASE)
    has_new = re.search(r'new', filename, re.IGNORECASE)
    has_delete = re.search(r'delete', filename, re.IGNORECASE)
    has_int = re.search(r'int', filename, re.IGNORECASE)
    has_int64 = re.search(r'int64', filename, re.IGNORECASE)
    has_long = re.search(r'long', filename, re.IGNORECASE)
    has_char = re.search(r'char', filename, re.IGNORECASE)
    has_wchar = re.search(r'wchar', filename, re.IGNORECASE)
    if has_fopen:
        return 'fopen'
    elif has_array:
        if has_class:
            return 'array_class'
        elif has_struct:
            return 'array_struct'
        elif has_int64:
            return 'array_int64'
        elif has_int:
            return 'array_int'
        elif has_long:
            return 'array_long'
        elif has_wchar:
            return 'array_wchar'
        elif has_char:
            return 'array_char'
        else:
            return ''
    elif has_new or has_delete:
        if has_class:
            return 'new_class'
        elif has_struct:
            return 'new_struct'
        elif has_int64:
            return 'new_int64'
        elif has_int:
            return 'new_int'
        elif has_long:
            return 'new_long'
        elif has_wchar:
            return 'new_wchar'
        elif has_char:
            return 'new_char'
        else:
            return ''
    else:
        if has_class:
            return 'class'
        elif has_struct:
            return 'struct'
        elif has_int64:
            return 'int64'
        elif has_int:
            return 'int'
        elif has_long:
            return 'long'
        elif has_wchar:
            return 'wchar'
        elif has_char:
            return 'char'
        else:
            return ''

def getCallback(file:str):
    global extern_path
    filename = os.path.basename(file)
    suffix = getSuffix(filename)
    if suffix == '':
        return ''
    else:
        return os.path.join(extern_path, f'callback_{suffix}.ll')

def getWorklist(src_path:str, mid_path:str, dst_path:str):
    worklist = dict()
    list(map(lambda x: setWorklist(x, src_path, mid_path, dst_path, worklist), os.listdir(src_path)))
    return worklist.values()

def runClang(compiler:str, input_file:str, output_file:str):
    global include_path, compile_fail
    print(f'-- Building {input_file}', end=' ')
    args = [compiler, '-O0', '-emit-llvm', '-S', '-g', '-I', include_path, input_file, '-o', output_file]
    result = subprocess.run(args, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    if result.returncode != 0:
        compile_fail.append(f'{input_file}\n')
        print('-- fail')
        return True
    else:
        print('-- done')
        return False

def runLink(linklist:list[str], file:str):
    global linkage_fail
    print(f'-- Linking {file}', end=' ')
    callback = getCallback(file)
    if callback == '':
        args = ['llvm-link', '-S']
    else:
        args = ['llvm-link', '-S', callback]
    args.extend(linklist)
    args.append('-o')
    args.append(file)
    result = subprocess.run(args, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    if result.returncode != 0:
        linkage_fail.append(f'{file}\n')
        print('-- fail')
        return True
    else:
        print('-- done')
        return False

def runWorklist(task:tuple[str, list, list, str]):
    compiler = task[0]
    src_file = task[1]
    mid_file = task[2]
    dst_file = task[3]
    result = list(map(lambda x, y: runClang(compiler, x, y), src_file, mid_file))
    if any(result):
        return True
    return runLink(mid_file, dst_file)

def runSection(benchmark:str, section_path:str, section:str):
    global build_path
    src_path = os.path.join(section_path, section)
    mid_path = os.path.join(build_path, benchmark, f'{section}-mid')
    dst_path = os.path.join(build_path, benchmark, f'{section}-ll')
    if not os.path.exists(mid_path):
        os.makedirs(mid_path)
    if not os.path.exists(dst_path):
        os.makedirs(dst_path)
    return any(list(map(runWorklist, getWorklist(src_path, mid_path, dst_path))))

def runBenchmark(benchmark:str):
    global bench_path
    section_path = os.path.join(bench_path, benchmark)
    if not os.path.isdir(section_path):
        return True
    return any(list(map(lambda x: runSection(benchmark, section_path, x), os.listdir(section_path))))

def runExternal(filename:str):
    global extern_path
    input_file = os.path.join(extern_path, filename)
    name, ext = os.path.splitext(filename)
    if ext == '.c':
        compiler = 'clang'
    elif ext == '.cpp':
        compiler = 'clang++'
    else:
        return
    output_file = os.path.join(extern_path, f'{name}.ll')
    return runClang(compiler, input_file, output_file)

def buildBenchmark(base_path:str):
    global bench_path, build_path, extern_path, include_path, compile_fail, linkage_fail
    compile_fail = list()
    linkage_fail = list()
    bench_path = os.path.join(base_path, 'benchmark')
    build_path = os.path.join(base_path, 'build')
    extern_path = os.path.join(base_path, 'external')
    include_path = os.path.join(base_path, 'include')
    time = datetime.datetime.now()
    stamp = f'log-{time.year}-{time.month}-{time.day}-{time.hour}-{time.minute}-{time.second}.txt'
    log_path = os.path.join(base_path, stamp)
    if os.path.exists(extern_path):
        list(map(runExternal, os.listdir(extern_path)))
    if os.path.exists(bench_path):
        list(map(runBenchmark, os.listdir(bench_path)))
    compile_len = len(compile_fail)
    linkage_len = len(linkage_fail)
    print(f'-> Compile Complete! (Compile Failure: {compile_len}, Linkage Failure: {linkage_len})')
    with open(log_path, 'wt') as fs:
        if (compile_len != 0):
            fs.write('Compile Failure:\n')
            fs.writelines(compile_fail)
        if (linkage_len != 0):
            fs.write('Linkage Failure:\n')
            fs.writelines(linkage_fail)
        if (compile_len == 0 and linkage_len == 0):
            fs.write('All Success!')