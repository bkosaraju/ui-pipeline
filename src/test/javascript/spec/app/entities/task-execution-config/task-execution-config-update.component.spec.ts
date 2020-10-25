import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PipelineTestModule } from '../../../test.module';
import { TaskExecutionConfigUpdateComponent } from 'app/entities/task-execution-config/task-execution-config-update.component';
import { TaskExecutionConfigService } from 'app/entities/task-execution-config/task-execution-config.service';
import { TaskExecutionConfig } from 'app/shared/model/task-execution-config.model';

describe('Component Tests', () => {
  describe('TaskExecutionConfig Management Update Component', () => {
    let comp: TaskExecutionConfigUpdateComponent;
    let fixture: ComponentFixture<TaskExecutionConfigUpdateComponent>;
    let service: TaskExecutionConfigService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PipelineTestModule],
        declarations: [TaskExecutionConfigUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(TaskExecutionConfigUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TaskExecutionConfigUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TaskExecutionConfigService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new TaskExecutionConfig(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new TaskExecutionConfig();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
