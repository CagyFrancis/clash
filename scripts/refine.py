import os
import re

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

def refine(source:str, suffix:str):
    sink = source.replace(r'/* POTENTIAL FLAW: No deallocation */', f'callback_{suffix}(data, no_free_{suffix});')
    if suffix in ['char', 'wchar', 'int', 'int64', 'long', 'struct']:
        result = sink.replace(r'free(data);', f'callback_{suffix}(data, has_free_{suffix});')
    elif suffix in ['array_char', 'array_wchar', 'array_int', 'array_int64', 'array_long', 'array_struct', 'array_class']:
        result = sink.replace(r'delete[] data;', f'callback_{suffix}(data, has_free_{suffix});')
        result = result.replace(r'delete [] data;', f'callback_{suffix}(data, has_free_{suffix});')
    elif suffix in ['new_char', 'new_wchar', 'new_int', 'new_int64', 'new_long', 'new_struct', 'new_class']:
        result = sink.replace(r'delete data;', f'callback_{suffix}(data, has_free_{suffix});')
    elif suffix == 'fopen':
        result = sink.replace(r'fclose(data);', f'callback_{suffix}(data, has_free_{suffix});')
    else:
        return sink
    return result

def runWorklist(src_path:str, dst_path:str, filename:str):
    src_file = os.path.join(src_path, filename)
    dst_file = os.path.join(dst_path, filename)
    suffix = getSuffix(filename)
    print(f'-- Refining {src_file}', end=' ')
    with open(src_file, 'rt') as fs:
        refined = refine(fs.read(), suffix)
    with open(dst_file, 'wt') as fs:
        fs.write(refined)
    print('-- done')

def runSection(benchmark:str, section_path:str, section:str):
    global refined_path
    src_path = os.path.join(section_path, section)
    dst_path = os.path.join(refined_path, benchmark, section)
    if not os.path.exists(dst_path):
        os.makedirs(dst_path)
    return any(list(map(lambda x: runWorklist(src_path, dst_path, x), os.listdir(src_path))))

def runBenchmark(benchmark:str):
    section_path = os.path.join(bench_path, benchmark)
    if not os.path.isdir(section_path):
        return True
    return any(list(map(lambda x: runSection(benchmark, section_path, x), os.listdir(section_path))))

def buildRefine(base_path:str):
    global bench_path, refined_path
    bench_path = os.path.join(base_path, 'benchmark')
    refined_path = os.path.join(base_path, 'refined')
    if os.path.exists(bench_path):
        list(map(runBenchmark, os.listdir(bench_path)))
    print(f'-> Refine Complete!')